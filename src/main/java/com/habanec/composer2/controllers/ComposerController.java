package com.habanec.composer2.controllers;

import com.habanec.composer2.assets.Form;
import com.habanec.composer2.assets.Grade;
import com.habanec.composer2.assets.Mode;
import com.habanec.composer2.assets.QuintCircle;
import com.habanec.composer2.services.ComposerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// makeUpMyComposition trpi tim, ze nezohlednuje locked measures - premicha harmonii.
// jedina neinvazivni zmena je tempo
// vse ostatni je treba promyslet. nemuzu mit zablokovane takty a menit starting grade nebo key
// co treba (je-li measure locked, nemuzu menit). jakmile zacnu carovat s harmonii, musi byt melodie festovni.
// chtel bych umet zmenu toniny (ne starting grade), tak aby nezmenila v harmonii ani field

// JS mi snad umozni zakliknuti lock a jeho okamzity zaznam do javy, stejne tak selecty...
// respektive tlacitko play by mohlo byt jako submit - vsechny zmeny sesbira najednou, nez spusti
// musim se naucit, jak sesbirat 30 cisel z 30 selectu do jednoho listu.

@Controller
@RequestMapping("/composer")
public class ComposerController extends BaseMidiComposerController {

    @Autowired
    public ComposerController(ComposerService composerService) {
        super(composerService);
    }

    @GetMapping({"", "/"})
    public String show(Model model) {
        model.addAttribute("files", composerService.getContentOfDirectory("./skladby/"));

        return "intro";
    }

    @GetMapping("/composition/new")
    public String newComposition(Model model) {
        model.addAttribute(
                "composition", composerService.composeANewOpus(
                        QuintCircle.C, Mode.MAJOR, Form.HOOK_D, Grade.I_
                ));

        return "index";
    }

    @GetMapping("/composition/load/{file}")
    public String loadComposition(Model model,
                                  @PathVariable(name = "file") String file) {
        model.addAttribute(
                "composition", composerService.loadCompositionFromTxt("skladby/" + file));
        return "index";
    }

    @GetMapping("/composition")
    public String showComposition(Model model) {
        model.addAttribute("composition", composerService.getMyComposition());
        return "index";
    }

    @GetMapping("/composition/play")
    public String playComposition(@RequestParam(name = "measure", required = false) Integer measureIndex,
                                  RedirectAttributes att) {
        composerService.playMyComposition(measureIndex);
        att.addFlashAttribute("message", "Playing...");

        return "redirect:/composer/composition";
    }

    @GetMapping("/composition/stop")
    public String stopPlayingComposition(RedirectAttributes att) {
        composerService.stopPlayingMyComposition();
        att.addFlashAttribute("message", "Stopped.");
        return "redirect:/composer/composition";
    }

    @GetMapping("/composition/save-doc")
    public String saveDocumentation(RedirectAttributes att) {
        String message = composerService.saveDocumentation();
        att.addFlashAttribute("message", message);
        return "redirect:/composer/composition";
    }

    @GetMapping("/composition/export-midi")
    public String exportMidi(RedirectAttributes att) {

        if (composerService.exportMidi()) {
            att.addFlashAttribute("message", "Successfully saved.");
        } else {
            att.addFlashAttribute("message", "Error, not saved.");
        }
        return "redirect:/composer/composition";
    }

    @PostMapping("/composition/change")
    public String makeChanges(@RequestParam Integer tempo,
                              @RequestParam(name = "key") Integer quintCircleIndex,
                              @RequestParam(name = "modus") Integer modeIndex,
                              @RequestParam Integer startingGrade,
                              @RequestParam(name = "isMelodyOn", defaultValue = "false") boolean isMelodyOn,
                              @RequestParam(name = "isAccompanimentOn", defaultValue = "false") boolean isAccompanimentOn,
                              RedirectAttributes att) {
        composerService.makeUpMyComposition(tempo, quintCircleIndex, modeIndex, startingGrade, isMelodyOn, isAccompanimentOn);
        att.addFlashAttribute("message", "Changes made.");
        return "redirect:/composer/composition";
    }

    @GetMapping("/composition/shiftDown")
    public String shiftDown(@RequestParam(name = "index") Integer measureIndex,
                            RedirectAttributes att) {
        composerService.shiftMeasureFirstTone(measureIndex, -1);
        att.addFlashAttribute("message", "Measure " + (measureIndex + 1) + " shifted down.");
        return "redirect:/composer/composition";
    }

    @GetMapping("/composition/shiftUp")
    public String shiftUp(@RequestParam(name = "index") Integer measureIndex,
                          RedirectAttributes att) {
        composerService.shiftMeasureFirstTone(measureIndex, 1);
        att.addFlashAttribute("message", "Measure " + (measureIndex + 1) + " shifted up.");
        return "redirect:/composer/composition";
    }


}
