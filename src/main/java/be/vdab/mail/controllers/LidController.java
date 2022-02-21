package be.vdab.mail.controllers;

import be.vdab.mail.domain.Lid;
import be.vdab.mail.exceptions.KanMailNietZendenException;
import be.vdab.mail.services.LidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("leden")
public class LidController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LidService lidService;

    public LidController(LidService lidService) {
        this.lidService = lidService;
    }

    @GetMapping("registratieform")
    public ModelAndView registratieform() {
        return new ModelAndView("registratieform").addObject(new Lid("", "", ""));
    }

    @PostMapping
    public String registreer(@Valid Lid lid, Errors errors, RedirectAttributes redirect, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "registratieform";
        }
        try {
            //System.out.println("CHECK THIS:" + request.getRequestURL().toString());
            //onderstaande verwijst naar de URL van de POST request: http://localhost:8080/leden (de link die je bezoekt na je submit)
            //overal URL!
            lidService.registreer(lid, request.getRequestURL().toString());
            redirect.addAttribute("id", lid.getId());
            return "redirect:/leden/geregistreerd/{id}";
        } catch (KanMailNietZendenException ex) {
            logger.error("Kan mail niet zenden", ex);
            return "redirect:/leden/nietgeregistreerd";
        }
    }
    @GetMapping("geregistreerd/{id}")
    public String geregistreerd(@PathVariable long id) {
        return "geregistreerd";
    }

    @GetMapping("nietgeregistreerd")
    public String nietGeregistreerd() {
        return "nietgeregistreerd";
    }

    @GetMapping("{id}")
    public ModelAndView info(@PathVariable long id) {
        var modelAndView = new ModelAndView("lidinfo");
        lidService.findById(id).ifPresent(lid -> modelAndView.addObject(lid));
        return modelAndView;
    }
}
