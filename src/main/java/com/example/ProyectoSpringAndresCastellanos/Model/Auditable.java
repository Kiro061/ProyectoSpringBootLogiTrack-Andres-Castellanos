package com.example.ProyectoSpringAndresCastellanos.Model;

public interface Auditable {
    Long getId();
    String getAuditData();
}