import paramiko
import os
import errno
import traceback
from stat import S_ISDIR, S_ISREG
from configparser import ConfigParser


# from utils.FileUtil import *

def direxists(sftp, path):
    """os.path.exists for paramiko's SCP object
    """
    try:
        sftp.stat(path)
    except IOError as e:
        if e.errno == errno.ENOENT:
            return False
        raise
    else:
        return True


class SftpUtil:
    def __init__(self, sftpConfig):
        cfg = ConfigParser()
        cfg.read(sftpConfig)
        self.hostname = cfg.get('sftp', 'hostname')
        self.port = cfg.getint('sftp', 'port')
        self.username = cfg.get('sftp', 'username')
        self.password = cfg.get('sftp', 'password')
        self.localpath = cfg.get('sftp', 'localpath')
        self.remotepath = cfg.get('sftp', 'remotepath')
        self.timeout = cfg.getint('sftp', 'timeout')

    def uploadFile(self, files):
        try:
            transport = paramiko.Transport((self.hostname, self.port))
            transport.banner_timeout = self.timeout
            transport.connect(username=self.username, password=self.password)

            sftp = paramiko.SFTPClient.from_transport(transport)
            for file in files:
                src_path = os.path.join(self.localpath, file).replace('\\', '/')
                des_path = os.path.join(self.remotepath, file).replace('\\', '/')
                # 判断文件路径是否存在 不存在则先创建路径
                print(des_path)
                # return
                dir_path = des_path[:des_path.rfind("/")]
                if not direxists(sftp, dir_path):
                    sftp.mkdir(des_path[:des_path.rfind("/")])
                sftp.put(src_path, des_path)
            for entry in sftp.listdir_attr(self.remotepath):
                mode = entry.st_mode
                if S_ISDIR(mode):
                    print(entry.filename + " is folder")
                elif S_ISREG(mode):
                    print(entry.filename + " is file")
            self.listDirsAndFiles(sftp)
            sftp.close()
        except Exception as e:
            traceback.print_exc()
            print(e)

    def listDirsAndFiles(self, sftp):
        print(self.remotepath)
        for entry in sftp.listdir_attr(self.remotepath.replace("\\", "/")):
            mode = entry.st_mode
            if S_ISDIR(mode):
                print(entry.filename + " is folder")
            elif S_ISREG(mode):
                print(entry.filename + " is file")
