package com.carolbarbosa.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HandleFileUpload {

    public List<String> readCSVFile(MultipartFile f){
        BufferedReader br;
        List<String> result = new ArrayList<>();
        try {
            String line;
            InputStream is = f.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }
}
