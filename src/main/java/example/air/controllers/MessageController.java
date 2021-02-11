package example.air.controllers;

import example.air.entity.Message;
import example.air.entity.Users;
import example.air.entity.dto.MessageDto;
import example.air.repositores.MessageRepository;
import example.air.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;


    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @AuthenticationPrincipal Users author,
            @RequestParam(required = false) String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MessageDto> page = messageService.messageList(pageable,filter,author);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal Users author,
            @RequestParam(required = false) String filter,
            @Valid() Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam MultipartFile file,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable)
            throws IOException {
        Page<MessageDto> page = messageService.messageList(pageable,filter,author);
        model.addAttribute("url", "/main");
        model.addAttribute("page", page);
        message.setAuthor(author);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);
            model.addAttribute("message", null);
            messageRepository.save(message);
            return "redirect:/main";
        }
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    private void saveFile(Message message, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal Users currentUser,
            @PathVariable Users author,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<MessageDto> page = messageService.messageListForUser(pageable,author);
        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("page", page);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("url", "/user-messages/" + author.getId());

        return "userMessages";
    }

    @PostMapping("/user-messages/{userId}")
    public String updateMessage(
            @AuthenticationPrincipal Users currentUser,
            @PathVariable Long userId,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            saveFile(message, file);
            messageRepository.save(message);
        }
        return "redirect:/user-messages/" + userId;
    }

     @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal Users currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<Users> likes = message.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
}
