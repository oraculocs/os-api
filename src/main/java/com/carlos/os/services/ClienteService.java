package com.carlos.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.os.domain.Cliente;
import com.carlos.os.domain.Pessoa;
import com.carlos.os.dtos.ClienteDTO;
import com.carlos.os.dtos.TecnicoDTO;
import com.carlos.os.repositories.ClienteRepository;
import com.carlos.os.repositories.PessoaRepository;
import com.carlos.os.services.exceptions.DataIntegratyViolationException;
import com.carlos.os.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	
	/*
	 * Buscar um Cliente pelo ID
	 */
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	/*
	 * Lista todos os Clientes
	 */
	public List<Cliente> findAll(){
		return repository.findAll();
	}
	
	/*
	 * Gravar um Cliente
	 */
	public Cliente create(ClienteDTO objDTO) {
		Cliente newObj = new Cliente(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone());
		if(findByCPF(objDTO) != null) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		
		return repository.save(newObj);
	}
	
	/*
	 * Atualiza um Cliente
	 */
	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		Cliente oldObj = findById(id);
		
		if(findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		
		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());
		
		return repository.save(oldObj);
	}
	
	/*
	 * Deletar um Cliente pelo ID
	 */
	public void delete(Integer id) {
		Cliente obj = findById(id);
		if(obj.getList().size() > 0) {
			throw new DataIntegratyViolationException("Pessoa possui Ordens de Serviço, não pode ser excluído!");
		}
		
		repository.deleteById(id);
	}
	
	
	/*
	 * Busca Tecnico pelo CPF
	 */
	private Pessoa findByCPF(ClienteDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCPF(objDTO.getCpf());
		if (obj != null) {
			return obj;
		}
		return null;
	}
	
	
}
