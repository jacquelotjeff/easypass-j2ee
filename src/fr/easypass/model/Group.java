package fr.easypass.model;

import java.util.HashSet;


public class Group {
	
	private String nom;
	private String description;
	private String logo;
	private HashSet<User> users;
	private HashSet<Password> passwords;
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public void addUser(User user){
		this.users.add(user);
	}
	
	public void removeUser(User user){
		this.users.remove(user);
	}
	
	public HashSet<User> getUsers(){
		return this.users;
	}
	
	public void addPassword(Password password){
		this.passwords.add(password);
		password.setOwner(this);
	}
	
	public void removePassword(Password password){
		this.passwords.remove(password);
		password.setOwner(null);
	}
	
	public HashSet<Password> getPasswords(){
		return this.passwords;
	}

}