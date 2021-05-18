package com.carlos.os.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.carlos.os.domain.Cliente;
import com.carlos.os.domain.OS;
import com.carlos.os.domain.Tecnico;
import com.carlos.os.domain.enums.Prioridade;
import com.carlos.os.domain.enums.Status;
import com.carlos.os.repositories.ClienteRepository;
import com.carlos.os.repositories.OSRepository;
import com.carlos.os.repositories.TecnicoRepository;

@Service
public class DBService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private OSRepository osRepository;
	
	
	public void instanciaDB() {
		
		Tecnico t1 = new Tecnico(null, "Valdir Cesar", "144.785.300-84", "(88) 98888-8888");
		Tecnico t2 = new Tecnico(null, "Cauê Bruno", "641.760.040-88", "(61) 98234-0598");
		Cliente c1 = new Cliente(null, "Betina Campos", "598.508.200-80", "(21) 98848-7898");
		Cliente c2 = new Cliente(null, "Alex Vargas", "724.659.410-89", "(21) 98123-7220");
		
		OS os1 = new OS(null, Prioridade.ALTA, "Teste create OD",  Status.ANDAMENTO, t1, c1);
		
		t1.getList().add(os1);
		c1.getList().add(os1);
		
		tecnicoRepository.saveAll(Arrays.asList(t1, t2));
		clienteRepository.saveAll(Arrays.asList(c1, c2));
		osRepository.saveAll(Arrays.asList(os1));
	}

}
