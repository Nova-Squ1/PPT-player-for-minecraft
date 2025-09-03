# ppt
- 服务端：Spigot/Paper 1.20.1（JDK 17）
## 安装
1. `mvn package` 得到 `target/wall-image-plugin-1.0.0.jar`，放入 `plugins/`。
2. 首次启动后到 `plugins/WallImage/images/` 放图片（建议 PNG/JPG）。ppt播放的图片名字应当为数字.png
3. 根据需要修改 `config.yml` 中的世界名/坐标/朝向。

## 指令
- `/placeimage <文件名>`：将该图片铺到配置区域（例如：`/placeimage poster.png`）。
- `/placeimage`：不带参数时使用 `config.yml` 的 `default-image`。
- `/placeimage clear`：清理区域内的物品展示框。
- `/placeimage reload`：重载配置。
- `/ppt begin`：播放第 1 张（1.png）。
- `/ppt up`：切换到上一张（如果存在）。
- `/ppt down`：切换到下一张（如果存在）。
- `/ppt 数字`：切换到第几张（如果存在）。
- `/ppt host`：获取主持人工具。
需要权限：`ppt.use`（默认 OP）。

## 注意
- 需要墙后方是**实心方块**，展示框才能挂上（默认选的是“北表面”，即朝 -Z）。
- 填写的坐标应当为的图片的实际位置 而非他背后墙的坐标
- 为获得无边框效果，将 `itemframe-visible: false`（默认）。

