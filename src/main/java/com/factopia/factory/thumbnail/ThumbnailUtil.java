package com.factopia.factory.thumbnail;

import com.factopia.factory.domain.FactorySection;
import com.factopia.factory.domain.FactorySite;
import com.factopia.factory.domain.FactoryZone;
import com.factopia.factory.domain.Object3D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;

public class ThumbnailUtil {
    private static final int FIXED_WIDTH = 400;
    private static final int FIXED_HEIGHT = 200;

    public static byte[] generateThumbnail(FactorySite factorySite,
                                           List<FactoryZone> zones,
                                           List<FactorySection> sections,
                                           List<Object3D> objects) {
        try {
            BufferedImage image = new BufferedImage(FIXED_WIDTH, FIXED_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = image.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double scaleX = (double) FIXED_WIDTH / factorySite.getTotalWidth();
            double scaleY = (double) FIXED_HEIGHT / factorySite.getTotalHeight();

            // ✅ 공장 부지 배경 그리기
            drawFactoryBackground(graphics, factorySite, scaleX, scaleY);

            // ✅ 공장 구역(반투명 박스) 그리기
            drawFactoryZones(graphics, zones, scaleX, scaleY);

            // ✅ Object3D를 FactorySection별로 그룹화
            Map<String, List<Object3D>> objectMap = groupObjectsBySection(objects);

            // ✅ 공장 사용 구역(FactorySection) 및 Object3D 그리기
            drawFactorySections(graphics, sections, objectMap, scaleX, scaleY);

            // ✅ 공장 이름 출력
            drawFactoryName(graphics, factorySite);

            graphics.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 공장 부지 배경 설정
    private static void drawFactoryBackground(Graphics2D graphics, FactorySite factorySite, double scaleX, double scaleY) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, FIXED_WIDTH, FIXED_HEIGHT);

        int siteWidth = (int) (factorySite.getTotalWidth() * scaleX);
        int siteHeight = (int) (factorySite.getTotalHeight() * scaleY);

        graphics.setColor(new Color(150, 150, 150)); // 공장 부지 색상
        graphics.fillRect(0, 0, siteWidth, siteHeight);
    }

    // 🔹 공장 구역(반투명 박스) 그리기
    private static void drawFactoryZones(Graphics2D graphics, List<FactoryZone> zones, double scaleX, double scaleY) {
        graphics.setStroke(new BasicStroke(1));

        for (FactoryZone zone : zones) {
            int x = (int) (zone.getFzXStart() * scaleX);
            int y = (int) (zone.getFzZStart() * scaleY);
            int w = (int) ((zone.getFzXEnd() - zone.getFzXStart()) * scaleX);
            int h = (int) ((zone.getFzZEnd() - zone.getFzZStart()) * scaleY);

            graphics.setColor(new Color(100, 100, 255, 80)); // 반투명 파란색
            graphics.fillRect(x, y, w, h);
            graphics.setColor(new Color(50, 50, 200)); // 테두리
            graphics.drawRect(x, y, w, h);
        }
    }

    // 🔹 Object3D를 FactorySection별로 그룹화
    private static Map<String, List<Object3D>> groupObjectsBySection(List<Object3D> objects) {
        Map<String, List<Object3D>> objectMap = new HashMap<>();

        for (Object3D obj : objects) {
            String sectionNo = obj.getFactorySectionNo();

            if (!objectMap.containsKey(sectionNo)) {
                objectMap.put(sectionNo, new ArrayList<>());
            }

            objectMap.get(sectionNo).add(obj);
        }

        return objectMap;
    }

    // 🔹 공장 사용 구역(FactorySection) 및 Object3D 그리기
    private static void drawFactorySections(Graphics2D graphics, List<FactorySection> sections,
                                            Map<String, List<Object3D>> objectMap, double scaleX, double scaleY) {
        for (FactorySection section : sections) {
            List<Object3D> objectList = objectMap.getOrDefault(section.getFactorySectionNo(), new ArrayList<>());

            int x = (int) (section.getFsXStart() * scaleX);
            int y = (int) (section.getFsZStart() * scaleY);
            int w = (int) ((section.getFsXEnd() - section.getFsXStart()) * scaleX);
            int h = (int) ((section.getFsZEnd() - section.getFsZStart()) * scaleY);

            if (!objectList.isEmpty()) {
                for (Object3D obj : objectList) {
                    graphics.setColor(Color.decode(obj.getColor()));
                    graphics.fillRect(x, y, w, h);

                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(x, y, w, h);
                }
            } else {
                graphics.setColor(Color.GRAY);
                graphics.fillRect(x, y, w, h);

                graphics.setColor(Color.BLACK);
                graphics.drawRect(x, y, w, h);
            }
        }
    }

    // 🔹 공장 이름 출력
    private static void drawFactoryName(Graphics2D graphics, FactorySite factorySite) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 12));
        graphics.drawString(factorySite.getFactorySiteName(), 10, 15);
    }
}
