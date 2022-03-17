package com.example.spamovil.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Users extends RealmObject {
    @PrimaryKey
    private String correo_user;

    private String nombre_user;
    private String apellido_p_user;
    private String apellido_m_user;
    private String direccion_user;
    private String sucursal_user;
    private String tipo_user;
    private String access_to_user;
    private Boolean activo_user;
    private String principal;

    public String getCorreo_user() {
        return correo_user;
    }

    public void setCorreo_user(String correo_user) {
        this.correo_user = correo_user;
    }

    public String getNombre_user() {
        return nombre_user;
    }

    public void setNombre_user(String nombre_user) {
        this.nombre_user = nombre_user;
    }

    public String getApellido_p_user() {
        return apellido_p_user;
    }

    public void setApellido_p_user(String apellido_p_user) {
        this.apellido_p_user = apellido_p_user;
    }

    public String getApellido_m_user() {
        return apellido_m_user;
    }

    public void setApellido_m_user(String apellido_m_user) {
        this.apellido_m_user = apellido_m_user;
    }

    public String getDireccion_user() {
        return direccion_user;
    }

    public void setDireccion_user(String direccion_user) {
        this.direccion_user = direccion_user;
    }

    public String getSucursal_user() {
        return sucursal_user;
    }

    public void setSucursal_user(String sucursal_user) {
        this.sucursal_user = sucursal_user;
    }

    public String getTipo_user() {
        return tipo_user;
    }

    public void setTipo_user(String tipo_user) {
        this.tipo_user = tipo_user;
    }

    public String getAccess_to_user() {
        return access_to_user;
    }

    public void setAccess_to_user(String access_to_user) {
        this.access_to_user = access_to_user;
    }

    public Boolean getActivo_user() {
        return activo_user;
    }

    public void setActivo_user(Boolean activo_user) {
        this.activo_user = activo_user;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
