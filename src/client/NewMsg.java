/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.Serializable;

/**
 *
 * @author Motaz
 */
public class NewMsg implements Serializable{
        String msg;
        public NewMsg(String m){
                msg = m;
        }
}
