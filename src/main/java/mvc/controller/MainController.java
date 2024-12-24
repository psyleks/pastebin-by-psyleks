package mvc.controller;

import jakarta.validation.Valid;
import mvc.domain.Message;
import mvc.domain.User;
import mvc.repos.MessageRepo;
import mvc.service.StorageService;
import mvc.util.DateFormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private StorageService storageService;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {

        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model, Sort sort) {
        sort = Sort.by(Sort.Order.desc("createdAt"));
        Iterable<Message> messages = filter.isEmpty() ? messageRepo.findAll(sort) : messageRepo.findByTag(filter, sort);

        List<String> formattedDates = new ArrayList<>();
        for (Message message : messages) {
            formattedDates.add(DateFormatterUtil.formatMessageDate(message.getCreatedAt()));
        }

        model.addAttribute("messages", messages);
        model.addAttribute("formattedDates", formattedDates);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            if (!file.isEmpty()) {
                String fileName = storageService.saveFile(file);
                message.setFilename(fileName);
            }

            model.addAttribute("message", null);
            messageRepo.save(message);

        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);

        return "redirect:/main";
    }

    @GetMapping("/main/{id}")
    public String post(@PathVariable Long id, Model model) {

        Optional<Message> message = messageRepo.findById(id);

        if (message.isPresent()) {

            String formattedDate = DateFormatterUtil.formatMessageDate(message.get().getCreatedAt());

            model.addAttribute("message", message.get());
            model.addAttribute("formattedDate", formattedDate);

            return "post";
        } else {
            return "postError";
        }
    }

}