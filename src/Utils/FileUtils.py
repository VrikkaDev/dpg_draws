import json
import os

from Utils.ConfigUtils import GetProjectRoot


def json_to_dict(json_file_path):
    rpath = os.path.join(GetProjectRoot(), json_file_path)
    with open(rpath, 'rb') as json_file:
        json_data = json.load(json_file)
    return json_data


def save_to_file(file_path, text):
    with open(file_path, "w") as outfile:
        outfile.write(text)
    return
