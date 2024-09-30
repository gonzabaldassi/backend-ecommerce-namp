package com.namp.ecommerce.controller;

import com.namp.ecommerce.dto.ComboWithItDTO;
import com.namp.ecommerce.error.InvalidFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.service.IComboService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the combos:"+e.getMessage());
        }
    }
    @GetMapping("comboWithProductCombo")
    public ResponseEntity<?> getCombosWithIt(){
        try{
            return ResponseEntity.ok(comboService.getCombosWithIt());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the combos:"+e.getMessage());
        }
    }
    @PostMapping("combo")
    public ResponseEntity<?> createCombo(@RequestParam("combo") String comboJson, @RequestParam("file") MultipartFile file){
        try{
            ComboWithItDTO createdComboDTO = comboService.saveComboWithIt(comboJson, file);

            if (createdComboDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("This combo already exists");
            }

            return ResponseEntity.ok(createdComboDTO);
        }catch (InvalidFileFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating the combo");
        }
    }

    @DeleteMapping("combo/{id}")
    public ResponseEntity<?> deleteCombo(@PathVariable long id){
        try{
            ComboDTO comboDTO = comboService.findById(id);

            if (comboDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The combo does not exist");
            }
            comboService.delete(comboDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting the combo: " + e.getMessage());
        }
    }

    @PutMapping("combo/{id}")
    public ResponseEntity<?> updateCombo(@PathVariable long id, @RequestParam("combo") String comboJson, @RequestParam(value = "file", required = false) MultipartFile file){
        try{
            ComboWithItDTO existingComboDTO = comboService.findByIdWithIt(id);

            if (existingComboDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The combo does not exist");
            }

            ComboWithItDTO updatedComboDTO = comboService.update(existingComboDTO, comboJson, file);

            if (updatedComboDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("The entered name already exists");
            }
            return ResponseEntity.ok(updatedComboDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating the combo: " + e.getMessage());
        }
    }
}