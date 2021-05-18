package com.carlos.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.os.domain.Pessoa;
import com.carlos.os.domain.Tecnico;
import com.carlos.os.dtos.TecnicoDTO;
import com.carlos.os.repositories.PessoaRepository;
import com.carlos.os.repositories.TecnicoRepository;
import com.carlos.os.services.exceptions.DataIntegratyViolationException;
import com.carlos.os.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	/*
	 * Busca um Tecnico pelo ID
	 */
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Tecnico.class.getName()));
	}

	/*
	 * Lista todos os Tecnicos
	 */
	public List<Tecnico> findAll() {
		return repository.findAll();
	}

	/*
	 * Salva um Técnico
	 */
	public Tecnico create(TecnicoDTO objDTO) {
		Tecnico newObj = new Tecnico(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone());
		if (findByCPF(objDTO) != null) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		return repository.save(newObj);

		// Outra forma de escrever o código acima
		// return repository.save(new Tecnico(null, objDTO.getNome(), objDTO.getCpf(),
		// objDTO.getTelefone()));
	}

	/*
	 * Atualiza um Tecnico
	 */
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		Tecnico oldObj = findById(id);

		if (findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());

		return repository.save(oldObj);
	}

	/*
	 * Deleta um Tecnico pelo ID
	 */
	public void delete(Integer id) {
		Tecnico obj = findById(id);
		if(obj.getList().size() > 0) {
			throw new DataIntegratyViolationException("Tecnico possui Ordens de Serviço, não pode ser excluído!");
		}
		repository.deleteById(id);
	}

	/*
	 * Busca Tecnico pelo CPF
	 */
	private Pessoa findByCPF(TecnicoDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCPF(objDTO.getCpf());
		if (obj != null) {
			return obj;
		}
		return null;
	}

}
