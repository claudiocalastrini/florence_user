package it.exercises.model.io;

public class ResponseUser {
	private UserOut user;
	private String esito;
	private String descrizioneErrore;
	
	
	public UserOut getUser() {
		return user;
	}
	public void setUser(UserOut user) {
		this.user = user;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public String getDescrizioneErrore() {
		return descrizioneErrore;
	}
	public void setDescrizioneErrore(String descrizioneErrore) {
		this.descrizioneErrore = descrizioneErrore;
	}
	
	

}
