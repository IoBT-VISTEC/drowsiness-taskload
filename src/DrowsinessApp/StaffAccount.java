/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DrowsinessApp;

import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author guygu
 */
public class StaffAccount {
    HashMap<String, char[]> accounts;
    
    public StaffAccount(){
        accounts = new HashMap<>();
        accounts.put("admin", "password".toCharArray());
    }
    
    public boolean addAccount(String id, String password){
        if(accounts.containsKey(id.toLowerCase())){
            return false;
        }else{
            accounts.put(id.toLowerCase(), password.toCharArray());
            return true;
        }
    }
    
    public boolean addAccount(String id, char[] password){
        if(accounts.containsKey(id.toLowerCase())){
            return false;
        }else{
            
            accounts.put(id.toLowerCase(), password);
            return true;
        }
    }
    
    public boolean isAuthen(String id, String password){
        return accounts.get(id.toLowerCase()) == (password.toCharArray());
    }
    
    public boolean isAuthen(String id, char[] password){
        return Arrays.equals(accounts.get(id.toLowerCase()), password);
    }
}
