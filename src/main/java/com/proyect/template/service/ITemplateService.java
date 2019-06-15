package com.proyect.template.service;

import java.util.List;

import com.proyect.template.entity.Entity1;

/**
 * Servicio interfaz de advertising manager
 * 
 * @author Javier Gonzalez
 */
public interface ITemplateService {
	public List<Entity1> getRandomAnuncios(List<Entity1> anunciosFromDB);
}
