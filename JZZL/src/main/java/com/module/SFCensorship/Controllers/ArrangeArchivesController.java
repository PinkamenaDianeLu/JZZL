package com.module.SFCensorship.Controllers;

import com.factory.BaseFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MrLu
 * @createTime 2020/10/8 15:37
 * @describe 卷整理
 */
@Controller
@RequestMapping("/ArrangeArchives")
public class ArrangeArchivesController  extends BaseFactory {
    private final String operModul = "ArrangeArchives";
}
