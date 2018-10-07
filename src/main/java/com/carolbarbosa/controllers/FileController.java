package com.carolbarbosa.controllers;

import com.carolbarbosa.models.Talk;
import com.carolbarbosa.service.AgendaService;
import com.carolbarbosa.service.TalkService;
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
import java.util.List;

@Controller
public class FileController {

    private final TalkService talkService;
    private final AgendaService agendaService;
    private final HandleFile handleFileUpload = new HandleFile();

    @Autowired
    private ServletContext servletContext;

    public FileController(TalkService talkService, AgendaService agendaService) {
        this.talkService = talkService;
        this.agendaService = agendaService;
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
            talkService.add(new Talk(columns[0], Double.parseDouble(columns[1]),
                    Integer.parseInt(columns[2])));
        }

        redirectAttributes.addFlashAttribute("message", "Arquivo " + file.getOriginalFilename() + " enviado com sucesso!");
        redirectAttributes.addFlashAttribute("talkList", talkService.findAll());
        return "redirect:/";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() {
        File file = null;
        InputStreamResource resource = null;
        MediaType mediaType = null;
        try {
            StringBuilder result = new StringBuilder();
            String filePath = System.getProperty("java.io.tmpdir") + "output.csv";
            new File(filePath).delete();

            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(result.toString().getBytes());
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

}