package org.romanchi.controllers;

import org.romanchi.Application;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ImageServlet extends HttpServlet {
    private static final Logger logger = Application.getContext().getBean(Logger.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        String imageUrl = req.getPathInfo().substring(1);
        File imageFile = new File("C:\\Users\\Roman\\IdeaProjects\\shop\\src\\main\\webapp\\resources\\images\\"+imageUrl);
        try{
            logger.finest(imageFile.getCanonicalPath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(imageFile));
            byte[] buffer = new byte[1048576];
            resp.setHeader("Content-Type", "image/png");
            ServletOutputStream servletOutputStream = resp.getOutputStream();
            while(bufferedInputStream.available()>0){
                bufferedInputStream.read(buffer);
                servletOutputStream.write(buffer);
                servletOutputStream.flush();
            }

        }catch (IOException ex){
            ex.printStackTrace();
            logger.log(Level.SEVERE, ex, ex::getLocalizedMessage);
            try {
                resp.sendError(SC_NOT_FOUND);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e, e::getLocalizedMessage);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
