/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SkillexBackend.SkillexBackendDemo.dao;

import com.SkillexBackend.SkillexBackendDemo.models.Eventos;
import com.SkillexBackend.SkillexBackendDemo.vo.EventosVO;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author jefer
 */
public interface EventosDao {
    public List<EventosVO> mostrar_eventos();
    public Object save(EventosVO evento);
    public Object deleteById(Integer id);
    public Object updateEvento(EventosVO evento);
    public EventosVO getEvent(Integer id);
    public EventosVO getLastEvent();
    public Object reporte(String format) throws FileNotFoundException, JRException;
}
