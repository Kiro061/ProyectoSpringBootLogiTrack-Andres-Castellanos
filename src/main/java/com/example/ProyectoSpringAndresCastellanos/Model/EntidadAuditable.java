package com.example.ProyectoSpringAndresCastellanos.Model;

import com.example.ProyectoSpringAndresCastellanos.Model.Listener.AuditoriaListener;
import jakarta.persistence.EntityListeners;

@EntityListeners(AuditoriaListener.class)
public abstract class EntidadAuditable {
}