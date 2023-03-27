import os


def getAllFiles(path):
    res = []
    for root, dirs, files in os.walk(path):
        print("root", root)  # 当前目录路径
        for mydir in dirs:
            for file in os.listdir(os.path.join(path, mydir)):
                res.append(os.path.join(mydir, file))
    return res
