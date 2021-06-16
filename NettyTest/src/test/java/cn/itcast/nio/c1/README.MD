## Channel 数据读写双通道
	FileChannel
	SocketChannel

## Buffer 内存数据缓冲区
	ByteBuffer
		MappedByteBuffer
		DirectByteBuffer
		HeapByteBuffer

## Selector 选择器

## ByteBuffer 正确使用姿势
```$xslt
1. 向 buffer 写入数据，例如调用 channel.read(buffer)
2. 调用 flip() 切换至读模式
3. 从 buffer 读取数据，例如调用 buffer.get()
4. 调用 clean() 或 compact() 切换至写模式
5. 重复 1~4 步骤
```
