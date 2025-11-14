package manyWorker.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Mensaje extends DomainEntity {

    @NotNull
    @ManyToOne
    private Actor remitente;
    
    @NotNull
    @ManyToOne
    private Actor destinatario;
    
    private Date fechaEnvio;
    
    @NotBlank
    private String asunto;
    
    @NotBlank
    private String cuerpo;

    public Mensaje(@NotNull Actor remitente, @NotNull Actor destinatario, Date fechaEnvio, 
                   @NotBlank String asunto, @NotBlank String cuerpo) {
        super();
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.fechaEnvio = fechaEnvio;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    public Mensaje() {
        super();
    }

    public Actor getRemitente() {
        return remitente;
    }

    public void setRemitente(Actor remitente) {
        this.remitente = remitente;
    }

    public Actor getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Actor destinatario) {
        this.destinatario = destinatario;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
}