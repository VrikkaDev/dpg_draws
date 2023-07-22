@echo off

echo Installing required Python packages...
pip install -r src/requirements.txt
echo.

echo Starting the Discord bot...
python src/main.py