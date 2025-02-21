package com.factopia.factory.thumbnail;

import com.factopia.factory.domain.FactorySection;
import com.factopia.factory.domain.FactorySite;
import com.factopia.factory.domain.FactoryZone;
import com.factopia.factory.domain.Object3D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

// 썸네일 생성(디스크 저장 없이 메모리에서 처리)
public class ThumbnailUtil {
    // 고정 크기
    private static final int FIXED_WIDTH = 200;
    private static final int FIXED_HEIGHT = 100;

    public static byte[] generateThumbnail(FactorySite factorySite,
                                           List<Object3D> objects) {
        try {
            // 1️⃣ 이미지 캔버스 생성
            BufferedImage image = new BufferedImage(FIXED_WIDTH, FIXED_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = image.createGraphics();

            // 공장 배경
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.fillRect(0, 0, FIXED_WIDTH, FIXED_HEIGHT);

            // 2️ 공장 크기 대비 축소 비율 계산 (200x100에 맞춤)
            double scaleX = (double) FIXED_WIDTH / factorySite.getTotalWidth();
            double scaleY = (double) FIXED_HEIGHT / factorySite.getTotalHeight();

            // 오브젝트 색상 반영
            for (Object3D obj : objects) {
                int x = (int) (obj.getXPosition() * scaleX);
                int y = (int) (obj.getYPosition() * scaleY);
                int w = (int) (obj.getXSize() * scaleX);
                int h = (int) (obj.getYSize() * scaleY);

                graphics.setColor(Color.decode(obj.getColor()));
                graphics.fillRect(x, y, w, h);
            }

            // 🔹 4️⃣ 공장 이름 출력
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Arial", Font.BOLD, 12));
            graphics.drawString(factorySite.getFactorySiteName(), 10, 15);

            graphics.dispose();

            // 이미지 → 바이트 배열 변환 후 반환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
