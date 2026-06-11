package io.castle.example.web;

import io.castle.example.config.Demo;
import io.castle.example.config.DemoEnv;
import io.castle.example.config.Demos;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Renders the server-side pages: the home grid, the per-workflow pages and the
 * webhooks page.
 */
@Controller
public class PageController {

    private final DemoEnv env;
    private final WebhookStore webhooks;

    public PageController(DemoEnv env, WebhookStore webhooks) {
        this.env = env;
        this.webhooks = webhooks;
    }

    // Default params rendered with every page.
    private void defaultParams(Model model) {
        model.addAttribute("castle_pk", env.castlePk());
        model.addAttribute("location", env.get("location"));
        model.addAttribute("demo_list", Demos.LIST);
        model.addAttribute("username", env.get("valid_username"));
        model.addAttribute("valid_username", env.get("valid_username"));
        model.addAttribute("valid_password", env.get("valid_password"));
        model.addAttribute("invalid_password", env.get("invalid_password"));
        model.addAttribute("valid_name", env.get("valid_name"));
        model.addAttribute("valid_user_id", env.get("valid_user_id"));
        model.addAttribute("webhook_url", env.get("webhook_url"));
    }

    @GetMapping("/")
    public String home(Model model) {
        defaultParams(model);
        model.addAttribute("home", true);
        return "demo";
    }

    @GetMapping("/webhooks")
    public String webhooks(HttpServletRequest req, Model model) {
        Demo d = Demos.byUrl("webhooks");
        defaultParams(model);
        model.addAttribute("demo_name", "webhooks");
        model.addAttribute("friendly_name", d.getFriendlyName());
        model.addAttribute("blurb", d.getBlurb());

        String proto = req.getHeader("X-Forwarded-Proto");
        if (proto == null || proto.isEmpty()) {
            proto = req.getScheme();
        }
        model.addAttribute("webhook_endpoint", proto + "://" + req.getHeader("host") + "/webhooks/castle");
        model.addAttribute("webhooks_received", webhooks.list());
        return "webhooks";
    }

    @GetMapping("/{demoName}")
    public String demo(@PathVariable String demoName, Model model, HttpServletResponse resp) {
        defaultParams(model);
        Demo d = Demos.byUrl(demoName);
        if (d == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("message", "Page not found");
            return "error";
        }
        model.addAttribute("demo_name", demoName);
        model.addAttribute("friendly_name", d.getFriendlyName());
        model.addAttribute("blurb", d.getBlurb());
        model.addAttribute("wsd", d.getWsd());
        return demoName;
    }
}
