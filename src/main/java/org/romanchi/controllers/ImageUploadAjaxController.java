package org.romanchi.controllers;

import com.oracle.webservices.internal.api.message.ContentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.romanchi.Message;
import org.romanchi.Wired;
import org.romanchi.database.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageUploadAjaxController implements Controller {
    @Wired
    Logger logger;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getParameterMap().forEach((key,val)->{logger.finest(key + " " + val);});
            JSONObject respJson = new JSONObject();
            for(Part part:request.getParts()){
                if(part.getContentType()!=null && part.getContentType().startsWith("image")){
                    String imageSrc = new Date().getTime()+new Random().nextInt(100)+".png";
                    part.write(imageSrc);
                    respJson.put("imageSrc",imageSrc);
                    break;
                }
            };
            return respJson.toJSONString();
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            Map<String, Object> respMap = new HashMap<>();
            respMap.put("status", "error");
            respMap.put("errorMessage", Message.SERVER_ERROR.getLocalized(request));
            logger.log(Level.SEVERE, e, e::getLocalizedMessage);
            return new JSONObject(respMap).toJSONString();
        }
    }
}
