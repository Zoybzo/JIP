from configparser import ConfigParser
from utils import FileUtil
from utils.SftpUtil import SftpUtil
from configparser import ConfigParser

from utils import FileUtil
from utils.SftpUtil import SftpUtil
import os
import requests

cfg = ConfigParser()
cfg.read('config/sftpConfig.ini')


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


if __name__ == "__main__":
    tgt = cfg.get('sftp', 'http-hostname') + ':' + cfg.get('sftp', 'springboot-port') + '/sftp/test'
    print(requests.post(tgt, data='placeholder').content)

    # res = getAllFiles(r'/root/projects/data/JIP/result', )
    # print(res)
    # cfg = ConfigParser()
    # sftp = SftpUtil('config/sftpConfig.ini')
    # sftp.uploadFile(FileUtil.getAllFiles(cfg.get('sftp', 'localpath')))
