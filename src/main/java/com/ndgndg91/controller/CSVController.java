package com.ndgndg91.controller;

import lombok.extern.log4j.Log4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


@Log4j
@Controller
public class CSVController {
    private static final String SAMPLE_CSV_FILE = "/sample.csv";

    @GetMapping(value = {"/print/path"})
    public void printPath(HttpServletRequest request) {
        log.info("/print/path");
        log.info(Paths.get(SAMPLE_CSV_FILE));
        log.info(System.getProperty("user.dir"));
        log.info(request.getServletContext().getRealPath("/"));
        log.info(request.getServletContext().getContextPath());
        log.info(request.getSession().getServletContext().getRealPath("/"));
        log.info(request.getSession().getServletContext().getContextPath());

        log.info(request.getServletPath());
        log.info(request.getServerName());
        log.info(request.getServerPort());
        log.info("end");
    }


    @GetMapping(value = {"/test/csv/write"})
    public void makeTestCSV(HttpServletRequest request) throws IOException {
        String uploadedPath = request.getServletContext().getRealPath("/") + "uploaded";
        File path = new File(uploadedPath);
        if (path.mkdirs())
            log.info(request.getServletContext().getRealPath("/") + "uploaded 생성 Success!");
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(uploadedPath + SAMPLE_CSV_FILE));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("ID", "Name", "Designation", "Company"))
        ) {
            csvPrinter.printRecord("1", "Sundar Pichai", "CEO", "Google");
            csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
            csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");

            csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

            csvPrinter.flush();
        }
    }

    @GetMapping(value = {"/test/csv/download"})
    public void downloadTestCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=sample.csv");
        String csvPath = request.getServletContext().getRealPath("/") + "uploaded" + SAMPLE_CSV_FILE;

        StringBuilder contents = new StringBuilder();
        contents.append("ID,Name,Designation,Company\n");
        Reader in = new FileReader(csvPath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader("ID", "Name", "Designation", "Company")
                .withFirstRecordAsHeader()
                .parse(in);

        for (CSVRecord strings : records) {
            for (String string : strings){
                contents.append(string);
                contents.append(",");
                log.info(string);
            }
            contents.replace(contents.length()-1, contents.length(), "");
            contents.append("\n");
        }

        log.info(contents.toString());
        response.getOutputStream().print(contents.toString());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
