package test.springboot.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.springboot.app.models.Cuenta;
import test.springboot.app.models.TransactionDto;
import test.springboot.app.services.CuentaService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Cuenta> listar() {
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Cuenta detalle(@PathVariable Long id) {
        return cuentaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Cuenta save(@RequestBody Cuenta cuenta) {
        return cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<Map<String, Object>> transferir(@RequestBody TransactionDto dto) {
        cuentaService.transferir(
            dto.getCuentaOrigenId(),
            dto.getCuentaDestinoId(),
            dto.getMonto(),
            dto.getBancoId()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito!");
        response.put("transaction", dto);

        return ResponseEntity.ok(response);
    }

}
