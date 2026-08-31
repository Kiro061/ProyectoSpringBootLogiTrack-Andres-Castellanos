package com.example.ProyectoSpringAndresCastellanos;

import com.example.ProyectoSpringAndresCastellanos.Repository.AuditoriaRepository;
import com.example.ProyectoSpringAndresCastellanos.Repository.BodegaRepository;
import com.example.ProyectoSpringAndresCastellanos.Repository.MovimientoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ProyectoSpringAndresCastellanosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoSpringAndresCastellanosApplication.class, args);
		System.out.println("HOLA MUNDO...");
	}
	@Bean
	CommandLineRunner testConexion(BodegaRepository bodegaRepository, MovimientoRepository movimientoRepository, AuditoriaRepository auditoriaRepository) {
		return args -> {
			System.out.println(">>> Bodegas encontradas en la BD:");
			bodegaRepository.findAll().forEach(b ->
					System.out.println(" - " + b.getId() + ": " + b.getNombre() + " (" + b.getUbicacion() + ")")
			);
			System.out.println("Movimientos: "
					+ movimientoRepository.findAll().size());

			System.out.println("Auditorías: "
					+ auditoriaRepository.findAll().size());
		};
	}

}