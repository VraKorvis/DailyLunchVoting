package ru.javapractice.dailylunchvoting.app;

public interface AuthUserProvider {
    AuthUser get();
    AuthUser safeGet();
}