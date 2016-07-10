package fr.easypass.utils;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.jasypt.util.text.StrongTextEncryptor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.easypass.manager.ConnectorManager;

public class Encryptor {
    
    String encryptedPassword;
    StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
    String password;
    
    public Encryptor() {
        
        InputStream stream = Encryptor.class.getClassLoader().getResourceAsStream("config.properties");
        JsonObject jsonObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        this.password = jsonObject.get("encryptor").getAsJsonObject().get("password").getAsString();
        
    }
    
    public void setPlainPassword(String plainPassword){
        
        textEncryptor.setPassword(this.password);
        this.encryptedPassword = textEncryptor.encrypt(plainPassword);
        
    }
    
    public String getEncryptedPassword(){
        
        return this.encryptedPassword;
        
    }
    
    public String decryptPassword(String password){
        
        textEncryptor.setPassword(this.password);
        return textEncryptor.decrypt(password);
        
    }

}
