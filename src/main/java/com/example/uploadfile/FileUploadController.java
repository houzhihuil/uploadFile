package com.example.uploadfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(name = "FileUploadController", urlPatterns = "/uploadFile")
public class FileUploadController extends HttpServlet {

    @Override
   /* protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the maximum allowed size of uploaded files
        long maxFileSize = 1024 * 1024 * 10; // 10MB

        // Set the directory used to store uploaded files
        String uploadDir = "/images";

        // Check if the request contains a file
        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);

            // If the file name is empty, skip this iteration
            if (fileName == null || fileName.isEmpty()) {
                continue;
            }

            // Check if the file size is less than or equal to the allowed maximum
            if (part.getSize() > maxFileSize) {
                request.setAttribute("message", "The file " + fileName + " exceeds the maximum allowed size.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Create a file to store the uploaded file
            File file = new File(uploadDir + File.separator + fileName);

            // Copy the contents of the uploaded file to the new file
            InputStream inputStream = part.getInputStream();
            file.getParentFile().mkdirs();
            file.createNewFile();
            java.nio.file.Files.copy(inputStream, file.toPath());

            // Set a success message
            request.setAttribute("message", "File uploaded successfully!");
        }

        // Forward to a success page
        request.getRequestDispatcher("success.jsp").forward(request, response);
    }
*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the file part from the request
        Part filePart = request.getPart("file");

        // Get the filename
        String fileName = extractFileName(filePart);
        // Get the path to the "images" directory
        String imagePath = request.getServletContext().getRealPath("/images");

        // Create a Path object for the file
        Path filePath = Paths.get(imagePath, fileName);

        // Check if the file already exists
        if (Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "File already exists");
            return;
        }

        // Copy the file to the images directory
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath);
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
