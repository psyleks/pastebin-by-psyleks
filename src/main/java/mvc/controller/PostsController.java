package mvc.controller;

import mvc.domain.Message;
import mvc.domain.User;
import mvc.repos.MessageRepo;
import mvc.security.UserService;
import mvc.util.DateFormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.ZoneId;
import java.util.Optional;

@Controller
@RequestMapping("/main/{uniqueId}")
public class PostsController {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getPost(@PathVariable String uniqueId, Model model) {

        Optional<Message> message = messageRepo.findByUniqueId(uniqueId);

        if (message.isPresent()) {

            ZoneId moscowZone = ZoneId.of("Europe/Moscow");
            String formattedDate = DateFormatterUtil.formatMessageDate(message.get().getCreatedAt(), moscowZone);

            model.addAttribute("message", message.get());
            model.addAttribute("formattedDate", formattedDate);

            return "post";
        } else {
            return "postError";
        }
    }

    @DeleteMapping
    @Transactional
    public String deletePost(@PathVariable String uniqueId, Principal principal) {
        Optional<Message> message = messageRepo.findByUniqueId(uniqueId);
        if (message.isEmpty()) {
            return "redirect:/postError";
        }

        User currentUser = userService.getUserByUsername(principal.getName());
        if (!message.get().getAuthor().equals(currentUser)) {
            return "redirect:/postError";
        }

        messageRepo.deleteByUniqueId(uniqueId);
        return "redirect:/main";
    }

}
