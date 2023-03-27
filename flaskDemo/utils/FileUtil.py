import os


def getAllFiles(path):
    res = []
    for root, dirs, files in os.walk(path):
        print("root", root)  # 当前目录路径
        for file in files:
            fileFullPath = os.path.join(root, file)
            print("path", path)
            fileSubPath = fileFullPath.replace(path + '/', '', 1)
            res.append(fileSubPath)
    return res
