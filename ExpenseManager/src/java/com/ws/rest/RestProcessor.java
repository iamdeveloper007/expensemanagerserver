/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.rest;

/**
 *
 * @author Comp
 */


import com.db.mysql.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This thread will run only in Cloud Connector. Responsible to handle requests
 * on LDAP Authentication, List and Search OUs, Restart and un registration of
 * Cloud connector plug-in.
 *
 * @author arun
 */
@RestController
public class RestProcessor {

    public static final Logger logger = LoggerFactory.getLogger(RestProcessor.class);
    
    /**
     * MySQL object will get initiated if it is Probe.
     */
    public static ConnectionUtil mysqlCon;

    
    static {
        try {
            mysqlCon = new ConnectionUtil();
        } catch (Exception ex) {
            System.out.println("Exception :: " + ex);
        }
    }

    @RequestMapping(value = {"/processpost/{functionName}"}, method = {RequestMethod.POST})
    public ResponseEntity<String> processServer(@PathVariable String functionName, RequestEntity<String> requestEntity) {
        String strReturn = "";

        try {
                strReturn = "Sucess";
        } catch (Exception e) {
            logger.error("Exception :: " + e);
            return new ResponseEntity(strReturn, HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(strReturn, HttpStatus.OK);
    }
}
