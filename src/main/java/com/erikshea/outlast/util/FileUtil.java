/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erikshea.outlast.util;

import com.erikshea.outlast.controller.AnimalControl;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author hoops
 */
public class FileUtil {
    public static URI getAssetsDir(){
        URI assetsDir = null;
        try {
            assetsDir = AnimalControl.class.getResource( "/assets/" ).toURI();
        } catch (URISyntaxException ex) {
        }
        return assetsDir;
    }
}
