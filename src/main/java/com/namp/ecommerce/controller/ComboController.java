package com.namp.ecommerce.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.model.Combo;
import com.namp.ecommerce.service.IComboService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api-namp")
public class ComboController {
@Autowired
    private IComboService comboService; 

    @GetMapping("combo")
    public ResponseEntity<?> getCombos(){
        try{
            return ResponseEntity.ok(comboService.getCombos());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al mostrar los Combos:"+e.getMessage());
        }
    }

    @GetMapping("comboWithProductCombo")
    public ResponseEntity<?> getCombosWithITProductCombos(){
        try{
            return ResponseEntity.ok(comboService.getCombosWithITProductCombos());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al mostrar los Combos:"+e.getMessage());
        }
    }

    @PostMapping("combo")
    public ResponseEntity<?> createCombo(@Valid @RequestBody ComboDTO comboDTO){
        try{
            ComboDTO createdComboDTO = comboService.save(comboDTO);

            if (createdComboDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Este combo ya se encuentra registrado");
            }

            return ResponseEntity.ok(createdComboDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el combo:"+e.getMessage());
        }
    }

    @DeleteMapping("combo/{id}")
    public ResponseEntity<?> deleteCombo(@PathVariable long id){
        try{
            ComboDTO comboDTO = comboService.findById(id);

            if (comboDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El combo no existe");
            }

            comboService.delete(comboDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el combo: " + e.getMessage());
        }
    }
    
    @PutMapping("combo/{id}")
    public ResponseEntity<?> updateCombo(@PathVariable long id, @Valid @RequestBody Combo combo){
        try{
            ComboDTO existingComboDTO = comboService.findById(id);

            if (existingComboDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El combo no existe");
            }

            ComboDTO updatedComboDTO = comboService.update(existingComboDTO, combo);

            if (updatedComboDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre ingresado ya existe");
            }

            return ResponseEntity.ok(updatedComboDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el combo: " + e.getMessage());
        }
    }
}
