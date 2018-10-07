package com.carolbarbosa.controllers;

import com.carolbarbosa.models.Talk;
import com.carolbarbosa.service.TalkService;
import com.carolbarbosa.util.HandleFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FileController {

    private final TalkService talkService;
    private HandleFileUpload handleFileUpload = new HandleFileUpload();

    public FileController(TalkService talkService) {
        this.talkService = talkService;
    }

    @GetMapping("/")
    public String uploadFile() {
        return "uploadFile";
    }

    @PostMapping("/")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        List<String> lines = handleFileUpload.readCSVFile(file);
        if(lines.size() > 1) lines.remove(0); //remove title
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }
}
