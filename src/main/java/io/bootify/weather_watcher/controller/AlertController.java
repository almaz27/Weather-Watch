package io.bootify.weather_watcher.controller;

import io.bootify.weather_watcher.domain.Sensor;
import io.bootify.weather_watcher.model.AlertDTO;
import io.bootify.weather_watcher.repos.SensorRepository;
import io.bootify.weather_watcher.service.AlertService;
import io.bootify.weather_watcher.util.CustomCollectors;
import io.bootify.weather_watcher.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;
    private final SensorRepository sensorRepository;

    public AlertController(final AlertService alertService,
            final SensorRepository sensorRepository) {
        this.alertService = alertService;
        this.sensorRepository = sensorRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("sensorValues", sensorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Sensor::getId, Sensor::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("alerts", alertService.findAll());
        return "alert/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("alert") final AlertDTO alertDTO) {
        return "alert/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("alert") @Valid final AlertDTO alertDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "alert/add";
        }
        alertService.create(alertDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("alert.create.success"));
        return "redirect:/alerts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("alert", alertService.get(id));
        return "alert/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("alert") @Valid final AlertDTO alertDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "alert/edit";
        }
        alertService.update(id, alertDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("alert.update.success"));
        return "redirect:/alerts";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        alertService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("alert.delete.success"));
        return "redirect:/alerts";
    }

}
