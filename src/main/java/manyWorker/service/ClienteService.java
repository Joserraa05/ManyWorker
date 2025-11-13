package manyWorker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import manyWorker.entity.Cliente;
import manyWorker.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public Optional<Cliente> findById(int id) {
		return this.clienteRepository.findById(id);
	}

	public List<Cliente> findAll() {
		return this.clienteRepository.findAll();
	}

	public Cliente save(Cliente cliente) {
		return this.clienteRepository.save(cliente);
	}

	// TODO; Solo el usuario propietario puede realizar esta accion
	public Cliente update(int idCliente, Cliente cliente) {
		Optional<Cliente> oCliente = findById(idCliente);
		if (oCliente.isPresent()) {
			Cliente c = oCliente.get();
			c.setNombre(cliente.getNombre());
			c.setApellido(cliente.getApellido());
			c.setApellido2(cliente.getApellido2());
			c.setCorreo(cliente.getCorreo());
			c.setFoto(cliente.getFoto());
			c.setTelefono(cliente.getTelefono());
			c.setDireccion(cliente.getDireccion());
			return save(c);
		}
		return null;
	}

	// TODO; Solo el usuario propietario puede realizar esta accion
	// TODO: Posteriormente se van a a anonimizar los datos en vez de eliminar.
	public void delete(int id) {
		this.clienteRepository.deleteById(id);
	}
	
	//Edicion de datos personales
	public Cliente updateCliente(int id, Cliente updatedCliente) {
		
		Optional<Cliente> optionalCliente = clienteRepository.findById(id);
		
		if(optionalCliente.isEmpty()){
			throw new RuntimeException("Cliente no encontrado");
		}
		
		Cliente existing = optionalCliente.get();
		
	    existing.setNombre(updatedCliente.getNombre());
	    existing.setApellido(updatedCliente.getApellido());
	    existing.setFoto(updatedCliente.getFoto());
	    existing.setCorreo(updatedCliente.getCorreo());
	    existing.setTelefono(updatedCliente.getTelefono());
	    existing.setNumeroPerfiles(updatedCliente.getNumeroPerfiles());
	    // otros campos personales
	    return clienteRepository.save(existing);
	}
	
	//Exportacion de datos personales
	public Map<String, Object> exportarDatos(int id) {
	    Cliente cliente = clienteRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
	    Map<String, Object> datos = new HashMap<>();
	    datos.put("nombre", cliente.getNombre());
	    datos.put("apellido", cliente.getApellido());
	    datos.put("foto", cliente.getFoto());
	    datos.put("correo", cliente.getCorreo());
	    datos.put("telefono", cliente.getTelefono());
	    datos.put("numero perfiles", cliente.getNumeroPerfiles());
	    // incluir datos de relaciones si aplica
	    return datos;
	}

	//Eliminacion de datos personales
	@Transactional
	public void eliminarCliente(int id) {
	    if (!clienteRepository.existsById(id)) {
	        throw new RuntimeException("Cliente no encontrado");
	    }
	    // Si tiene datos relacionados (solicitudes, mensajes, etc.), eliminarlos aquí
	    clienteRepository.deleteById(id);
	}

}
