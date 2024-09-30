package com.kse.core.authkeycloak.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
@Slf4j
public class FileTools {

    /**
     * Cette fonction permet de créer un fichier
     * @param file
     * @param title
     * @param path
     * @param id
     * @param dateString
     * @return
     */
    public String createImage(MultipartFile file, String filename, String path) {
        try {
            log.info("Creation fichier: title={}; path={}; dateString={}",filename, path);
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            filename = filename+"."+ext;
            File newimgname = new File( filename );
            file.transferTo(new File(path+ newimgname));
            log.info("Creation  fichier terminée: title={}; path={};",filename, path);
            return  path + newimgname.getName();

        } catch (Exception e) {
            // TODO: handle exception
            log.error("ERROR création fichier: title={}; path={} |  error={}",filename, path, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cette fonction est utilisée pour supprimer un fichier
     * @param filepath
     */
    public void deleteFile(String filepath) {
        try {
            log.info("Suppression d'un fichier: filepath={}", filepath);
            if(filepath != null) {
                File img = new File(filepath);
                if(img.delete()){
                    log.info(filepath + " est supprimé.");
                }else{
                    log.info("Opération de suppression echouée: filepath={}", filepath);
                }
            }
        }catch (Exception e) {
            // TODO: handle exception
            log.error( "Error: " +e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isValid(MultipartFile multipartFile) {

        boolean result = false;

        if(multipartFile != null){
            String contentType = multipartFile.getContentType();
            if (isSupportedContentType(contentType)) {
                result = true;
            }
        }
        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return  contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }


}
