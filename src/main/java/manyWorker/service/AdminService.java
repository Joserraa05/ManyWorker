package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import manyWorker.entity.Admin;
import manyWorker.entity.Roles;
import manyWorker.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	public Optional<Admin> findById(int id) {
		return this.adminRepository.findById(id);
	}

	public List<Admin> findAll() {
		return this.adminRepository.findAll();
	}

	public Admin save(Admin admin) {
		admin.setRol(Roles.ADMINISTRADOR);
		admin.setAuthority("ADMINISTRADOR");
        
        if (admin.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
		
		return this.adminRepository.save(admin);
	}

	// FIX: update NO reencripta el password. Guarda directamente con repository.
	public Admin update(int idAdmin, Admin admin) {
		Optional<Admin> oAdmin = findById(idAdmin);
		if (oAdmin.isPresent()) {
			Admin a = oAdmin.get();
			a.setNombre(admin.getNombre());
			a.setApellido(admin.getApellido());
			a.setApellido2(admin.getApellido2());
			a.setCorreo(admin.getCorreo());
			a.setFoto(admin.getFoto());
			a.setTelefono(admin.getTelefono());
			a.setDireccion(admin.getDireccion());
			// NO llamamos a save() porque reencriptaría el password
			return this.adminRepository.save(a);
		}
		return null;
	}

	public void delete(int id) {
		this.adminRepository.deleteById(id);
	}
	
	public boolean existsById(int id) {
	    return this.adminRepository.existsById(id);
	}

	public Optional<Admin> findByUsername(String username) {
		return adminRepository.findByUsername(username);
	}
}