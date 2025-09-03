# ppt
- 服务端：Spigot/Paper 1.20.1（JDK 17）
## 安装
1. `mvn package` 得到 `target/wall-image-plugin-1.0.0.jar`，放入 `plugins/`。
2. 首次启动后到 `plugins/WallImage/images/` 放图片（建议 PNG/JPG）。
3. 根据需要修改 `config.yml` 中的世界名/坐标/朝向。

## 指令
- `/placeimage <文件名>`：将该图片铺到配置区域（例如：`/placeimage poster.png`）。
- `/placeimage`：不带参数时使用 `config.yml` 的 `default-image`。
- `/placeimage clear`：清理区域内的物品展示框。
- `/placeimage reload`：重载配置。
- `/ppt begin`：播放第 1 张（1.png）。
- `/ppt up`：切换到上一张（如果存在）。
- `/ppt down`：切换到下一张（如果存在）。

需要权限：`ppt.use`（默认 OP）。

## 注意
- 需要墙后方是**实心方块**，展示框才能挂上（这里选的是“北表面”，即朝 -Z）。
- 为获得无边框效果，将 `itemframe-visible: false`（默认）。
- 如果你要改尺寸，请同时修改 `ImageTiler.WIDTH_TILES / HEIGHT_TILES` 与配置区域的格数。

## 备注
- AI真好用吧 能ai就别手写