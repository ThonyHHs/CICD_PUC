package dev.thony.flight_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.thony.flight_api.model.DTOs.RouteDTO;
import dev.thony.flight_api.model.Enums.ContinentEnum;
import dev.thony.flight_api.service.RouteService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController()
@RequestMapping("/api/route")
public class RouteController {
    private RouteService routeService;

    RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping()
    public RouteDTO getRoute(@RequestParam String airplaneCode, @RequestParam ContinentEnum continent) {
        return routeService.generateRoute(airplaneCode, continent);
    }
    
}
