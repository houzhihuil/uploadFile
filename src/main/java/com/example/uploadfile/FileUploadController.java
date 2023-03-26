package com.example.uploadfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(name = "FileUploadController", urlPatterns = "/uploadFile")
public class FileUploadController extends HttpServlet {

    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the file part from the request
        Part filePart = request.getPart("file");

        // Get the filename
        String fileName = extractFileName(filePart);
        // Get the path to the "images" directory
         String imagePath = request.getServletContext().getRealPath("/images");


        String basePath = request.getServletContext().getRealPath("/");
        String uploadPath = Paths.get(basePath, "uploads").toString();
        System.out.println("********");

        System.out.println(basePath);
        System.out.println(uploadPath);

        // Create a Path object for the file
        Path filePath = Paths.get(imagePath, fileName);

        // Check if the file already exists
        if (Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "File already exists");
            return;
        }

        // Copy the file to the images 2 directories

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath);

            // Reset the input stream to the beginning of the file
            input.reset();

            String filePath2 = "C:\\Users\\houzh\\IdeaProjects\\uploadFile\\src\\main\\webapp\\images\\";
            Path path2 = Paths.get(filePath2);

            // Create the images directory if it does not exist
            Files.createDirectories(path2);

            try (OutputStream output = new FileOutputStream(filePath2 + filePart.getSubmittedFileName())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
        }



        // Set the content type and response message
        response.setContentType("text/plain");
        response.getWriter().println("File uploaded successfully!");
    }

    private String extractFileName(Part part) {
        String contentDispositionHeader = part.getHeader("content-disposition");
        String[] elements = contentDispositionHeader.split(";");

        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}














