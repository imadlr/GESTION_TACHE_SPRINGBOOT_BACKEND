package com.gt.gestion_taches.web;

import com.gt.gestion_taches.dtos.AgendaDTO;
import com.gt.gestion_taches.dtos.CountTaskByStateDTO;
import com.gt.gestion_taches.dtos.PageTaskDTO;
import com.gt.gestion_taches.enums.TaskState;
import com.gt.gestion_taches.exceptions.NoAgendaFoundException;
import com.gt.gestion_taches.exceptions.UserNotFoundException;
import com.gt.gestion_taches.servicesImpl.AgendaServiceImpl;
import com.gt.gestion_taches.servicesImpl.TaskServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/resp")
@AllArgsConstructor
public class ResponsibleRestController {
    private AgendaServiceImpl agendaService;
    private TaskServiceImpl taskService;

    @GetMapping("/countByState")
    public List<CountTaskByStateDTO> getCountOfTasksByState() {
        return taskService.getCountOfTasksByState();
    }

    @GetMapping("/agendaByDay/{responsibleId}")
    public List<AgendaDTO> getAgendaByDay(@PathVariable Long responsibleId,
                                          @RequestParam(name = "jour", defaultValue = "") String jour) throws UserNotFoundException, NoAgendaFoundException {
        if (jour.isEmpty()) {
            // Utiliser la date d'aujourd'hui comme valeur par défaut
            LocalDate dateAujourdhui = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
            jour = dateAujourdhui.format(formatter);
        }
        return agendaService.getByResponsibleIdAndDay(responsibleId, jour);
    }

    @GetMapping("/agendaByDate/{responsibleId}")
    public List<AgendaDTO> getAgendaByDate(@PathVariable Long responsibleId, @RequestParam LocalDate date) throws UserNotFoundException {
        return agendaService.getByResponsibleIdAndDate(responsibleId, date);
    }

    @PutMapping("/finishedAgenda/{agendaId}")
    public void finishedAgenda(@PathVariable Long agendaId) {
        agendaService.finishedAgenda(agendaId);
    }

    @GetMapping("/completedTasks")
    public PageTaskDTO getCompletedTasks(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        return taskService.getByStateAndObjectContains(TaskState.TERMINEE, keyword, page, size);
    }

    @GetMapping("/currentTasks")
    public PageTaskDTO getCurrentTasks(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        return taskService.getByStateAndObjectContains(TaskState.EN_ATTENTE, keyword, page, size);
    }

    @GetMapping("/lateTasks")
    public PageTaskDTO getLateTasks(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        return taskService.getByStateAndObjectContains(TaskState.EN_RETARD, keyword, page, size);
    }

}
