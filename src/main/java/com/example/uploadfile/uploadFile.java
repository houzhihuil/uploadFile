/*
package com.example.uploadfile;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class uploadFile {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Set the directory used to temporarily store files that are larger than the configured size threshold
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Set the maximum allowed size of uploaded files
        upload.setSizeMax(1024 * 1024 * 10); // 10MB

        try {
            // Parse the request to get the list of file items
            List<FileItem> items = upload.parseRequest(request);

            // Process the uploaded file items
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    // Get the name of the uploaded file
                    String fileName = new File(item.getName()).getName();

                    // Write the uploaded file to the server
                    File uploadedFile = new File("uploads/" + fileName);
                    item.write(uploadedFile);

                    // Set a success message
                    request.setAttribute("message", "File uploaded successfully!");
                }
            }
        } catch (Exception ex) {
            // Set an error message
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }

        // Forward to a success page
        request.getRequestDispatcher("success.jsp").forward(request, response);
    }

}
*/
package com.example.uploadfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class uploadFile {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
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
