/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aegean.eIdEuSmartClass.model;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.aegean.eIdEuSmartClass.model.User;
import java.util.List;

/**
 *
 * @author Dante
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    List<User> findAll();
}
