package com.example.file_upload_demo.repository;

public interface IPredictionRepository { /**
 * ユーザー名とパスワードをAuthUserテーブルに記録する
 *
 * @param path パス
 * @param prediction 推論結果
 * @return データベースの更新行数
 */
    public int insert(String path, int prediction);
}
