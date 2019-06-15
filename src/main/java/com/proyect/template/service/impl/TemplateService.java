package com.proyect.template.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.proyect.template.entity.Entity1;
import com.proyect.template.service.ITemplateService;

/**
 * Servicio general que tiene todo el manejo para la obtencion de anuncios
 * 
 * @author Javier Gonzalez
 *
 */
@Service
public class TemplateService implements ITemplateService {

	private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

	/**
	 * Metodo que obtiene los 3 anuncios
	 */
	@Override
	public List<Entity1> getRandomAnuncios(List<Entity1> anunciosFromDB) {
		logger.info("getRandomAnuncios...");
		List<Entity1> resultingAnuncios = new ArrayList<Entity1>();
		// Compute the total weight of all items together
		double totalWeight = 0.0d;
		for (Entity1 i : anunciosFromDB) {
			totalWeight += i.getCostoImpresion();
		}

		Entity1 randomAnuncio = null;
		boolean presupuestoImpresionNoAlcanzado = false;
		int cantidadDeImpresionesRestantes = 0;
		boolean logicaDeFechasImpresion = false;
		boolean impresionNoVencida = false;
		String[] fechaArray;

		LocalDate dateToday = LocalDate.now();
		LocalDate dateFinImpresion = null;

		// Establecemos un limite de ratio de impresion dias para tratar que no se agote
		// antes de tiempo.
		double thresholdImpresion = 0.25;

		// Mientras sea menor a 3 sigo agregando
		while (resultingAnuncios.size() < 3) {
			randomAnuncio = getRandomAnuncio(totalWeight, anunciosFromDB);

			String fechaFinalizacion = randomAnuncio.getFechaFinalizacion();

			try {
				fechaArray = fechaFinalizacion.split("/");
				int year = Integer.valueOf(fechaArray[2]);
				int month = Integer.valueOf(fechaArray[1]);
				int day = Integer.valueOf(fechaArray[0]);

				dateFinImpresion = LocalDate.of(year, month, day);

			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			// Dias restantes para que la publicacion no se deba imprimir mas.
			long daysBetween = ChronoUnit.DAYS.between(dateToday, dateFinImpresion);

			// Aca calculamos si nos pasamos del presupuesto, si nos pasamos no se imprime
			// mas
			presupuestoImpresionNoAlcanzado = ((randomAnuncio.getNumeroImpresiones() + 1)
					* randomAnuncio.getCostoImpresion()) < randomAnuncio.getCostoTotalMaximo();

			// Aca trataremos de no quedarnos sin impresiones antes de la fecha
			cantidadDeImpresionesRestantes = (int) (randomAnuncio.getCostoTotalMaximo()
					/ (((randomAnuncio.getNumeroImpresiones() == 0 ? 1 : randomAnuncio.getNumeroImpresiones()))
							* randomAnuncio.getCostoImpresion()));

			// La idea aca es que vayan de la mano la cantidad de impresiones restantes con
			// los dias que quedan.

			if (cantidadDeImpresionesRestantes >= daysBetween) {
				logicaDeFechasImpresion = true;
			} else {
				if (thresholdImpresion > (cantidadDeImpresionesRestantes / daysBetween)) {
					logicaDeFechasImpresion = true;
				} else {
					logicaDeFechasImpresion = false;
				}
			}

			// La impresion no debe estar vencida
			impresionNoVencida = daysBetween >= 0;

			// Solo agrego si ya no esta, hay que pensar que quizas el random traiga el
			// mismo mas de una vez
			if (!resultingAnuncios.contains(randomAnuncio) && presupuestoImpresionNoAlcanzado && logicaDeFechasImpresion
					&& impresionNoVencida) {
				resultingAnuncios.add(randomAnuncio);
			}
		}

		return resultingAnuncios;
	}

	/**
	 * Metodo que calcula y obtiene un anuncio basado en su peso.
	 * 
	 * @param totalWeight
	 * @param anunciosFromDB
	 * @return
	 */
	private Entity1 getRandomAnuncio(double totalWeight, List<Entity1> anunciosFromDB) {
		logger.info("getRandomAnuncio...");
		double random = Math.random() * totalWeight;
		for (Entity1 anuncio : anunciosFromDB) {
			random -= anuncio.getCostoImpresion();
			if (random <= 0.0d) {
				return anuncio;
			}
		}
		return null;
	}
}
