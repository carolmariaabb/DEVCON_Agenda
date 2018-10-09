package com.carolbarbosa.controllers;

import com.carolbarbosa.models.AgendaItem;
import com.carolbarbosa.models.Talk;
import com.carolbarbosa.service.AgendaItemService;
import com.carolbarbosa.service.TalkService;
import com.carolbarbosa.util.AgendaSolver;
import com.carolbarbosa.util.HandleFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
public class FileController {

    private final TalkService talkService;
    private final AgendaItemService agendaItemService;
    private final HandleFile handleFileUpload = new HandleFile();
    private static final String CR_LF = "\r\n";
    private Integer highPriority = 0;

    @Autowired
    private ServletContext servletContext;

    public FileController(TalkService talkService, AgendaItemService agendaItemService) {
        this.talkService = talkService;
        this.agendaItemService = agendaItemService;
    }

    @GetMapping("/")
    public String uploadFile() {
        return "uploadFile";
    }

    @PostMapping("/deleteTalks")
    public String deleteTalks() {
        talkService.removeAll();
        return "redirect:/";
    }

    @PostMapping(value = "/")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
                                                          HttpServletRequest request) {
        List<String> lines = handleFileUpload.readCSVFile(file);
        if (lines.size() > 1) lines.remove(0); //remove cabecalho

        //criando lista de palestras
        talkService.removeAll();
        for (String line : lines) {
            String[] columns = line.split(";");
            if (columns.length < 3) break;
            talkService.add(new Talk(columns[0], Integer.parseInt(columns[1]), Integer.parseInt(columns[2]), false));
        }
        if(talkService.count() < 1) return "redirect:/";
        talkService.sortByPriorityDesc();

        //Criacao de agend
        List<Talk> talksAuxList = new ArrayList<>(talkService.findAll());
        List<AgendaItem> agenda = new AgendaSolver().createAgenda(talksAuxList, 1);
        //seta palestras que ja estao na agenda
        for(AgendaItem a : agenda){
            talkService.getByIndex(a.getIdTalk()).setIsOnAgenda(true);
        }
        talksAuxList = new ArrayList<>(talkService.findAll().stream().filter(not(Talk::getIsOnAgenda)).collect(Collectors.toList()));
        List<AgendaItem> agendaDay2 = new AgendaSolver().createAgenda(talksAuxList, 2);
        agenda.addAll(agendaDay2);
        agendaItemService.setAgenda(agenda);

        talksAuxList = new ArrayList<>(talkService.findAll().stream().filter(not(Talk::getIsOnAgenda)).collect(Collectors.toList()));

        redirectAttributes.addFlashAttribute("message", "Arquivo " + file.getOriginalFilename() + " enviado com sucesso!");
        redirectAttributes.addFlashAttribute("talkList", talkService.findAll());
        redirectAttributes.addFlashAttribute("talksNotOnList", talksAuxList);
        return "redirect:/";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() {
        File file = null;
        InputStreamResource resource = null;
        MediaType mediaType = null;
        try {
            String filePath = System.getProperty("java.io.tmpdir") + "output.csv";
            new File(filePath).delete();

            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(getOutputCSV().getBytes());
            fos.close();

            file = new File(filePath);
            mediaType = handleFileUpload.getMediaTypeForFileName(this.servletContext, filePath);
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType).contentLength(file.length()).body(resource);
    }

    public String getOutputCSV(){
        DecimalFormat formatter = new DecimalFormat("00");
        StringBuilder result = new StringBuilder();
        result.append("day;start;end;title");
        result.append(CR_LF);
        for(AgendaItem agendaItem : agendaItemService.findAll()){
            String hourStart = formatter.format(agendaItem.getStart()/60);
            String minuteStart = formatter.format(agendaItem.getStart() % 60);

            String hourEnd = formatter.format(agendaItem.getEnd()/60);
            String minuteEnd = formatter.format(agendaItem.getEnd() % 60);

            result.append(agendaItem.getDay() + ";" + hourStart + ":" + minuteStart + ";" + hourEnd
                    + ":" + minuteEnd + ";" + agendaItem.getTitle());
            result.append(CR_LF);
        }
        return result.toString();
    }

    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}